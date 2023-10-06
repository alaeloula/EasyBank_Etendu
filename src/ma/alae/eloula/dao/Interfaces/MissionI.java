package ma.alae.eloula.dao.Interfaces;

import ma.alae.eloula.classes.Mission;

public interface MissionI {
    boolean ajouterMission(Mission mission);
     Mission findMissionById(int missionId);
     boolean supprimerMission(int missionId);

}
