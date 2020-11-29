package com.djk.tournament_manager.tools;

import com.djk.tournament_manager.dao.match.MatchDAO;
import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;

import java.util.*;
import java.util.stream.Collectors;

public class StableRoommates {
    private final MatchDAO matchDAO;
    private final Tournament tournament;
    private final ArrayList<Player> playerList;
    private final HashMap<String, ArrayList<Match>> previousMatchesMap;
    private HashMap<String, ArrayList<String>> prefsMap;
    private HashMap<String, String> proposals;
    private HashMap<String, String> accepts;

    public StableRoommates(MatchDAO matchDAO, Tournament tournament, ArrayList<Player> playerList) {
        this.matchDAO = matchDAO;
        this.tournament = tournament;
        this.playerList = playerList;
        this.prefsMap = new HashMap<>();
        this.proposals = new HashMap<>();
        this.accepts = new HashMap<>();
        this.previousMatchesMap = new HashMap<>();
        buildPreviousMatchesMap();
    }

    /**
     * Add each player's list of possible pairings to preferences map by player ID
     */
    public void buildPrefsMap() {
        this.prefsMap = new HashMap<>();
        for(Player player : this.playerList) {
            // Add all other players to the pref list
//            HashMap<Long, ArrayList<Player>> opponentsByPrevMatchCount = new HashMap<>();
            ArrayList<ArrayList<Player>> opponentsByPrevMatchCount = new ArrayList<>();
            ArrayList<Match> playerPrevMatches = this.previousMatchesMap.get(player.getID());

            for(Player opponent : this.playerList) {
                if(!player.equals(opponent)) {
                    int numPreviousMatches = (int) playerPrevMatches.stream().filter(match -> match.playerIsInMatch(opponent.getID())).count();
//                    ArrayList<Player> opponentsForCount = opponentsByPrevMatchCount.containsKey(numPreviousMatches) ? opponentsByPrevMatchCount.get(numPreviousMatches) : new ArrayList<>();

                    // Make sure the array is indexed deep enough
                    while(opponentsByPrevMatchCount.size() - 1 < numPreviousMatches) {
                        opponentsByPrevMatchCount.add(new ArrayList<>());
                    }
                    ArrayList<Player> opponentsForCount = opponentsByPrevMatchCount.get(numPreviousMatches);
                    opponentsForCount.add(opponent);
                    opponentsByPrevMatchCount.set(numPreviousMatches, opponentsForCount);
                }
            }

            // Sort preferences by their difference in points, putting rematches at end of list
            ArrayList<Player> opponents;
            ArrayList<String> opponentIDs = new ArrayList<>();
            for(int i = 0; i < opponentsByPrevMatchCount.size(); i++) {
                opponents = opponentsByPrevMatchCount.get(i);
                opponents.sort(Comparator.comparingInt(opponent -> Math.abs(opponent.getPoints() - player.getPoints())));
                opponentIDs.addAll(opponents.stream().map(Player::getID).collect(Collectors.toCollection(ArrayList::new)));
            }

            this.prefsMap.put(player.getID(), opponentIDs);
        }
    }

    /**
     * Builds map of all player's previous matches in the tournament
     */
    public void buildPreviousMatchesMap() {
        ArrayList<Match> allPrevMatches = matchDAO.selectMatchesInTournament(this.tournament.getID());
        for(Match match : allPrevMatches) {
            ArrayList<Match> p1PrevMatches = previousMatchesMap.containsKey(match.getPlayer1ID()) ? previousMatchesMap.get(match.getPlayer1ID()) : new ArrayList<>();
            ArrayList<Match> p2PrevMatches = previousMatchesMap.containsKey(match.getPlayer2ID()) ? previousMatchesMap.get(match.getPlayer2ID()) : new ArrayList<>();

            p1PrevMatches.add(match);
            p2PrevMatches.add(match);

            previousMatchesMap.put(match.getPlayer1ID(), p1PrevMatches);
            previousMatchesMap.put(match.getPlayer2ID(), p2PrevMatches);
        }
    }

    /**
     * Use Stable Roommates algorithm to find the best pairings for all players
     * @return Array of best paired matches
     */
    public ArrayList<Match> getPairings() {
        sortPlayerList();

        // This is the first round, use completely random pairings
        if(tournament.getCurrRound() == 1) {
            return quickPair();
        }

        return generatePairings();
    }

    /**
     * Remove players from each others preferences list
     * @param p1ID player to be removed from player2's prefs list
     * @param p2ID player to be removed from player1's prefs list
     */
    private void rejectSymmetrically(String p1ID, String p2ID) {
        ArrayList<String> p1Prefs = this.prefsMap.get(p1ID);
        ArrayList<String> p2Prefs = this.prefsMap.get(p2ID);

        p1Prefs.remove(p2ID);
        p2Prefs.remove(p1ID);

        this.prefsMap.put(p1ID, p1Prefs);
        this.prefsMap.put(p2ID, p2Prefs);
    }

    /**
     * Add preferred to players proposal list and add player to preferred accepted list
     * @param playerID Proposing player
     * @param preferredID Player being proposed to
     */
    private void acceptProposal(String playerID, String preferredID) {
        proposals.put(playerID, preferredID);
        accepts.put(preferredID, playerID);
    }

    private boolean wouldBreakup(String playerID, String preferredID) {
        ArrayList<String> preferredPrefs = prefsMap.get(preferredID);

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

    private boolean firstPhase() {
        String playerID;
        String preferredID;
        String preferredOldAccept;
        ArrayList<String> unmatched = playerList.stream().map(Player::getID).collect(Collectors.toCollection(ArrayList::new));

        while(unmatched.size() > 0) {
            playerID = unmatched.get(0);
            if(prefsMap.get(playerID).size() > 0) {
                preferredID = prefsMap.get(playerID).get(0);
            } else {
                return false;
            }

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

        return true;
    }

    private void removePlayersLessPreferredThanAccepted() {
        ArrayList<String> toRemove;
        for (Player player : playerList) {
            int acceptIndex = accepts.containsKey(player.getID()) ? prefsMap.get(player.getID()).indexOf(accepts.get(player.getID())) : -1;
            if(acceptIndex > -1) {
                toRemove = prefsMap.get(player.getID());
                for(int i = acceptIndex + 1; i < toRemove.size(); i++) {
                    rejectSymmetrically(player.getID(), toRemove.get(i));
                }
            }
        }
    }

    private RotationChoices getRotation(String playerID, RotationChoices rotationChoices) {
        if(rotationChoices.lastChoices.size() > 1 && rotationChoices.lastChoices.subList(0, rotationChoices.lastChoices.size() - 2).contains(rotationChoices.lastChoices.get(rotationChoices.lastChoices.size() - 1))) {
            return rotationChoices;
        }

        String second;
        if (prefsMap.get(playerID).size() > 1) {
            second = prefsMap.get(playerID).get(1);
        } else {
            return new RotationChoices(null, null);
        }

        rotationChoices.secondChoices.add(second);
        int secondLastIndex = prefsMap.get(second).size() - 1;
        String secondLast = prefsMap.get(second).get(secondLastIndex);
        rotationChoices.lastChoices.add(secondLast);

        return getRotation(secondLast, rotationChoices);
    }

    private void removeRotation(RotationChoices rotationChoices) {
        int i = rotationChoices.lastChoices.indexOf(rotationChoices.lastChoices.get(rotationChoices.lastChoices.size() - 1));

        for(i++; i < rotationChoices.lastChoices.size(); i++) {
            rejectSymmetrically(rotationChoices.secondChoices.get(i - 1), rotationChoices.lastChoices.get(i));
        }
    }

    private boolean secondPhase() {
        String playerID;
        RotationChoices rotationChoices;
        int i = 0;

        while(i < playerList.size()) {
            playerID = playerList.get(i).getID();

            if(prefsMap.get(playerID).size() == 1) {
                i++;
            } else {
                rotationChoices = getRotation(playerID, new RotationChoices(new ArrayList<>(), new ArrayList<>(Collections.singleton(playerID))));

                if(rotationChoices.secondChoices == null || rotationChoices.lastChoices == null) {
                    return false;
                }

                removeRotation(rotationChoices);

                for(Player player : playerList) {
                    if(prefsMap.get(player.getID()).size() == 0) {
                        return false;
                    }
                }
                i = 0;
            }
        }

        return true;
    }

    private boolean computeMatches() {
        boolean stablePairingsExist = firstPhase();

        if(stablePairingsExist) {
            removePlayersLessPreferredThanAccepted();
            stablePairingsExist = secondPhase();
        }

        return stablePairingsExist;
    }

    private ArrayList<Match> generatePairings() {
        buildPrefsMap();
        if(computeMatches()) {
            return createMatchList();
        }

        return new ArrayList<>();
    }

    private ArrayList<Match> createMatchList() {
        String opponentID;
        ArrayList<String> prefsList;
        int tableNum = 1;
        ArrayList<Match> matches = new ArrayList<>();

        for(Player player : playerList) {
            if(prefsMap.containsKey(player.getID())) {
                prefsList = prefsMap.get(player.getID());

                if (prefsList.size() > 0) {
                    opponentID = prefsList.get(0);
                    matches.add(new Match(tournament, tableNum++, player.getID(), opponentID));

                    prefsMap.remove(player.getID());
                    prefsMap.remove(opponentID);
                }
            }
        }

        return matches;
    }

    private ArrayList<Match> quickPair() {
        int tableNum = 1;
        ArrayList<Match> matches = new ArrayList<>();

        for (int i = 0; i < this.playerList.size(); i += 2) {
            String p1ID = this.playerList.get(i).getID();
            String p2ID = this.playerList.get(i + 1).getID();

            Match newMatch = new Match(this.tournament.getID(), this.tournament.getNumGames(), p1ID, p2ID, tableNum++, this.tournament.getCurrRound());
            matches.add(newMatch);
        }

        return matches;
    }

    /**
     * Pair winning players with winning players and "randomize" players in each tier by sorting on the UUID assigned to the players ID
     */
    private void sortPlayerList() {
        // Secondary sort: randomly in asc or desc by ID
        Random rand = new Random();
        int randInt = rand.nextInt(100);
        boolean sortAsc = randInt % 2 == 1;
        if (sortAsc)
        {
            playerList.sort(Comparator.comparing(Player::getID));
        }
        else
        {
            playerList.sort(Comparator.comparing(Player::getID).reversed());
        }

        // Primary sort: players point totals
        playerList.sort(Comparator.comparing(Player::getPoints).reversed());
    }

//    private ArrayList<Match> oldPairingAlgo() {
//        boolean pairingSuccess;
//        boolean acceptNonUniqueOpponents = false;
//        int failedPairingsCount = 0;
//        int tableNum = 1;
//        ArrayList<Match> matches = new ArrayList<>();
//
//        for (int i = 0; i < this.playerList.size(); i += 2) {
//            pairingSuccess = true;
//            String p1ID = this.playerList.get(i).getID();
//            String p2ID = this.playerList.get(i + 1).getID();
//            ArrayList<Match> p1PrevMatches = this.matchDAO.selectAllMatchesByPlayerID(p1ID);
//
//            // Have these players played each other yet?
//            if(!acceptNonUniqueOpponents && p1PrevMatches.stream().anyMatch(match -> match.getPlayer1ID().equals(p2ID) || match.getPlayer2ID().equals(p2ID))) {
//                // Find the first unpairing player that p1 has not paired against yet
//                // We want to leave the top players with their best match opponent and rearrange the lower standings if possible
//                boolean swapPlayers = false;
//                int opponentIndex = findUnmatchedOpponent(this.playerList, p1ID, i, this.playerList.size());
//                Player opponent = new Player();
//
//                int playerIndex = i + 1;
//                Player player = this.playerList.get(playerIndex);
//
//                if (opponentIndex > -1) {
//                    // These players have not faced each other, swap their opponents and repair the match
//                    opponent = this.playerList.get(opponentIndex);
//                    swapPlayers = true;
//                    i-=2;
//
//                    // Have we tried the every combination of pairings? Short circuit this logic and repair solely on standings
//                    acceptNonUniqueOpponents = failedPairingsCount == this.playerList.size() * (this.playerList.size() - 1);
//
//                } else {
//                    // All unpaired players faced this player find a paired player
//                    // Find the first unpairing player that p1 has not paired against yet
//                    opponentIndex = findUnmatchedOpponent(this.playerList, p1ID, 0, i);
//                    if (opponentIndex > -1) {
//                        // These players have not faced each other, swap their opponents
//                        opponent = this.playerList.get(opponentIndex);
//                        swapPlayers = true;
//
//                        // We need to repair all players, clear everything and restart
//                        i = 0;
//                        tableNum = 0;
//                        matches = new ArrayList<>();
//                        failedPairingsCount++;
//
//                        // Have we tried the every combination of pairings? Short circuit this logic and repair solely on standings
//                        acceptNonUniqueOpponents = failedPairingsCount == this.playerList.size() * (this.playerList.size() - 1);
//                    }
//                }
//
//                if (swapPlayers) {
//                    this.playerList.set(opponentIndex, player);
//                    this.playerList.set(playerIndex, opponent);
//
//                    pairingSuccess = false;
//                }
//            }
//
//            if (pairingSuccess){
//                Match newMatch = new Match(this.tournament.getID(), this.tournament.getNumGames(), p1ID, p2ID, tableNum++, this.tournament.getCurrRound());
//                matches.add(newMatch);
//            }
//        }
//
//        return matches;
//    }
//
//    private int findUnmatchedOpponent(ArrayList<Player> waitingPlayers, String playerIDToMatch, int fromIndex, int toIndex)
//    {
//
//        for (int playerIndex = fromIndex; playerIndex < toIndex; playerIndex++) {
//            if (this.matchDAO.selectAllMatchesByPlayerID(waitingPlayers.get(playerIndex).getID())
//                    .stream()
//                    .noneMatch(match -> match.getPlayer1ID().equals(playerIDToMatch) || match.getPlayer2ID().equals(playerIDToMatch)))
//            {
//                return playerIndex;
//            }
//        }
//
//        return -1;
//    }
}

class RotationChoices {
    protected ArrayList<String> secondChoices;
    protected ArrayList<String> lastChoices;

    RotationChoices(ArrayList<String> secondChoices, ArrayList<String> lastChoices) {
        this.secondChoices = secondChoices;
        this.lastChoices = lastChoices;
    }
}
