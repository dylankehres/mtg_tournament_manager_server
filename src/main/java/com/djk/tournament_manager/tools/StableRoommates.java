package com.djk.tournament_manager.tools;

import com.djk.tournament_manager.dao.match.MatchDAO;
import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

public class StableRoommates {
    private final MatchDAO matchDAO;
    private final Tournament tournament;
    private final ArrayList<Player> playerList;
    private final HashMap<String, ArrayList<Match>> previousMatchesMap;
    private HashMap<String, ArrayList<String>> prefsTable;
    private HashMap<String, String> proposals;
    private HashMap<String, String> accepts;

    public StableRoommates(MatchDAO matchDAO, Tournament tournament, ArrayList<Player> playerList) {
        this.matchDAO = matchDAO;
        this.tournament = tournament;
        this.playerList = playerList;
        this.prefsTable = new HashMap<>();
        this.proposals = new HashMap<>();
        this.accepts = new HashMap<>();
        this.previousMatchesMap = new HashMap<>();
        buildPreviousMatchesMap();
    }

    public void buildPrefsTable(int numPrevMatchesAllowed) {
        this.prefsTable = new HashMap<>();
        for(Player player : this.playerList) {
            // Add all other players to the pref list
            ArrayList<Player> opponents = new ArrayList<>();
            ArrayList<Match> playerPrevMatches = this.previousMatchesMap.get(player.getID());

            for(Player opponent : this.playerList) {
                if(!player.equals(opponent)) {
                    if (playerPrevMatches.stream().filter(match -> match.playerIsInMatch(opponent.getID())).count() < numPrevMatchesAllowed) {
//                    if (playerPrevMatches.stream().noneMatch(match -> match.playerIsInMatch(opponent.getID()))) {
                        opponents.add(opponent);
                    }
                }
            }

            // Sort preferences by their difference in points
            opponents.sort(Comparator.comparingInt(opponent -> Math.abs(opponent.getPoints() - player.getPoints())));
            ArrayList<String> opponentIDs = opponents.stream().map(Player::getID).collect(Collectors.toCollection(ArrayList::new));
            this.prefsTable.put(player.getID(), opponentIDs);
        }
    }

    public void buildPreviousMatchesMap() {
        ArrayList<Match> allPrevMatches = matchDAO.selectMatchesInTournament(this.tournament.getID());
        for(Match match : allPrevMatches) {
            ArrayList<Match> p1PrevMatches = this.previousMatchesMap.get(match.getPlayer1ID());
            ArrayList<Match> p2PrevMatches = this.previousMatchesMap.get(match.getPlayer2ID());

            p1PrevMatches.add(match);
            p2PrevMatches.add(match);

            this.previousMatchesMap.put(match.getPlayer1ID(), p1PrevMatches);
            this.previousMatchesMap.put(match.getPlayer2ID(), p2PrevMatches);
        }
    }

    public ArrayList<Match> getPairings() {
        boolean useSRP = false;

        if(useSRP) {
            return generatePairings();
        }

        return oldPairingAlgo();
    }

    private void rejectSymmetrically(String p1ID, String p2ID) {
        ArrayList<String> p1Prefs = this.prefsTable.get(p1ID);
        ArrayList<String> p2Prefs = this.prefsTable.get(p2ID);

        p1Prefs.remove(p2ID);
        p2Prefs.remove(p1ID);

        this.prefsTable.put(p1ID, p1Prefs);
        this.prefsTable.put(p2ID, p2Prefs);
    }

    private void acceptProposal(String playerID, String preferredID) {
        proposals.put(playerID, preferredID);
        accepts.put(preferredID, playerID);
    }

    private boolean wouldBreakup(String playerID, String preferredID) {
        ArrayList<String> preferredPrefs = prefsTable.get(preferredID);

        int currProposalIndex = accepts.containsKey(preferredID) ? preferredPrefs.indexOf(accepts.get(preferredID)) : -1;
        int newProposalIndex = preferredPrefs.indexOf(playerID);

        return newProposalIndex < currProposalIndex;
    }

    private String breakupFor(String playerID, String preferredID) {
        proposals.put(playerID, preferredID);
        String oldAcceptID = accepts.get(preferredID);
        accepts.put(preferredID, playerID);
        rejectSymmetrically(preferredID, oldAcceptID);

        return oldAcceptID;
    }

    private void firstPhase() {
        String playerID;
        String preferredID;
        String preferredOldAccept;
        ArrayList<String> unmatched = playerList.stream().map(Player::getID).collect(Collectors.toCollection(ArrayList::new));

        while(unmatched.size() > 0) {
            playerID = unmatched.get(0);
            preferredID = prefsTable.get(playerID).get(0);

            if(!accepts.containsKey(preferredID)) {
                acceptProposal(playerID, preferredID);
                unmatched.remove(playerID);
            } else if (wouldBreakup(playerID, preferredID)) {
                preferredOldAccept = breakupFor(playerID, preferredID);
                unmatched.set(0, preferredOldAccept);
            } else {
                rejectSymmetrically(playerID, preferredID);
            }
        }
    }

    private void secondPhase() {
        ArrayList<String> toRemove;
        for (Player player : playerList) {
            int acceptIndex = accepts.containsKey(player.getID()) ? prefsTable.get(player.getID()).indexOf(accepts.get(player.getID())) : -1;
            if(acceptIndex > -1) {
                toRemove = prefsTable.get(player.getID());
                for(int i = acceptIndex + 1; i < toRemove.size(); i++) {
                    rejectSymmetrically(player.getID(), toRemove.get(i));
                }
            }
        }
    }

    private RotationChoices getRotation(String playerID, RotationChoices rotationChoices) {
        if(rotationChoices.lastChoices.subList(0, rotationChoices.lastChoices.size() - 2).contains(rotationChoices.lastChoices.get(rotationChoices.lastChoices.size() -1))) {
            return rotationChoices;
        }

        String second = "";
        if (prefsTable.get(playerID).size() > 1) {
            second = prefsTable.get(playerID).get(1);
        } else {
            return new RotationChoices(null, null);
        }

        rotationChoices.secondChoices.add(second);
        int secondLastIndex = prefsTable.get(second).size() - 1;
        String secondLast = prefsTable.get(second).get(secondLastIndex);
        rotationChoices.lastChoices.add(secondLast);

        return getRotation(secondLast, rotationChoices);
    }

    private void removeRotation(RotationChoices rotationChoices) {
        int i = rotationChoices.lastChoices.indexOf(rotationChoices.lastChoices.get(rotationChoices.lastChoices.size() - 1));

        for(i++; i < rotationChoices.lastChoices.size(); i++) {
            rejectSymmetrically(rotationChoices.secondChoices.get(i - 1), rotationChoices.lastChoices.get(i));
        }
    }

    private boolean thirdPhase() {
        String playerID;
        RotationChoices rotationChoices;
        int i = 0;

        while(i < playerList.size()) {
            playerID = playerList.get(i).getID();

            if(prefsTable.get(playerID).size() == 1) {
                i++;
            } else {
                rotationChoices = getRotation(playerID, new RotationChoices(new ArrayList<>(), new ArrayList<>(Collections.singleton(playerID))));

                if(rotationChoices.secondChoices == null || rotationChoices.lastChoices == null) {
                    return false;
                }

                removeRotation(rotationChoices);

                for(Player player : playerList) {
                    if(prefsTable.get(player.getID()).size() == 0) {
                        return false;
                    }
                }
                i = 0;
            }
        }

        return true;
    }

    public ArrayList<Match> generatePairings() {
        return new ArrayList<>();
    }

    public ArrayList<Match> oldPairingAlgo() {
        boolean pairingSuccess;
        boolean acceptNonUniqueOpponents = false;
        int failedPairingsCount = 0;
        int tableNum = 1;
        ArrayList<Match> matches = new ArrayList<>();

        for (int i = 0; i < this.playerList.size(); i += 2) {
            pairingSuccess = true;
            String p1ID = this.playerList.get(i).getID();
            String p2ID = this.playerList.get(i + 1).getID();
            ArrayList<Match> p1PrevMatches = this.matchDAO.selectAllMatchesByPlayerID(p1ID);

            // Have these players played each other yet?
            if(!acceptNonUniqueOpponents && p1PrevMatches.stream().anyMatch(match -> match.getPlayer1ID().equals(p2ID) || match.getPlayer2ID().equals(p2ID))) {
                // Find the first unpairing player that p1 has not paired against yet
                // We want to leave the top players with their best match opponent and rearrange the lower standings if possible
                boolean swapPlayers = false;
                int opponentIndex = findUnmatchedOpponent(this.playerList, p1ID, i, this.playerList.size());
                Player opponent = new Player();

                int playerIndex = i + 1;
                Player player = this.playerList.get(playerIndex);

                if (opponentIndex > -1) {
                    // These players have not faced each other, swap their opponents and repair the match
                    opponent = this.playerList.get(opponentIndex);
                    swapPlayers = true;
                    i-=2;

                    // Have we tried the every combination of pairings? Short circuit this logic and repair solely on standings
                    acceptNonUniqueOpponents = failedPairingsCount == this.playerList.size() * (this.playerList.size() - 1);

                } else {
                    // All unpaired players faced this player find a paired player
                    // Find the first unpairing player that p1 has not paired against yet
                    opponentIndex = findUnmatchedOpponent(this.playerList, p1ID, 0, i);
                    if (opponentIndex > -1) {
                        // These players have not faced each other, swap their opponents
                        opponent = this.playerList.get(opponentIndex);
                        swapPlayers = true;

                        // We need to repair all players, clear everything and restart
                        i = 0;
                        tableNum = 0;
                        matches = new ArrayList<>();
                        failedPairingsCount++;

                        // Have we tried the every combination of pairings? Short circuit this logic and repair solely on standings
                        acceptNonUniqueOpponents = failedPairingsCount == this.playerList.size() * (this.playerList.size() - 1);
                    }
                }

                if (swapPlayers) {
                    this.playerList.set(opponentIndex, player);
                    this.playerList.set(playerIndex, opponent);

                    pairingSuccess = false;
                }
            }

            if (pairingSuccess){
                Match newMatch = new Match("", this.tournament.getID(), this.tournament.getNumGames(), p1ID, p2ID, tableNum++, this.tournament.getCurrRound());
                matches.add(newMatch);
            }
        }

        return matches;
    }

    public int findUnmatchedOpponent(ArrayList<Player> waitingPlayers, String playerIDToMatch, int fromIndex, int toIndex)
    {

        for (int playerIndex = fromIndex; playerIndex < toIndex; playerIndex++) {
            if (this.matchDAO.selectAllMatchesByPlayerID(waitingPlayers.get(playerIndex).getID())
                    .stream()
                    .noneMatch(match -> match.getPlayer1ID().equals(playerIDToMatch) || match.getPlayer2ID().equals(playerIDToMatch)))
            {
                return playerIndex;
            }
        }

        return -1;
    }
}

class RotationChoices {
    protected ArrayList<String> secondChoices;
    protected ArrayList<String> lastChoices;

    RotationChoices(ArrayList<String> secondChoices, ArrayList<String> lastChoices) {
        this.secondChoices = secondChoices;
        this.lastChoices = lastChoices;
    }
}
