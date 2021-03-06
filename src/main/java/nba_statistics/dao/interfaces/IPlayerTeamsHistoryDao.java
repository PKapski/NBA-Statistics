package nba_statistics.dao.interfaces;


import nba_statistics.entities.PlayerTeamsHistory;

public interface IPlayerTeamsHistoryDao {
    void persist(PlayerTeamsHistory entity);
    void savePlayerTeamsHistory(String playerName , String teamName , String seasonName);
    void saveNewPlayerTeamsHistory(String playerName, String teamName, String seasonName);
}
