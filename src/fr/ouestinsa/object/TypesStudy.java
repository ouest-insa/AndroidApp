package fr.ouestinsa.object;

public enum TypesStudy {
	SITE_WEB(2), LOGICIEL(4), INFORMATIQUE(6), TRADUCTION_TECHNIQUE(8), 
	AUTOMATISATION_DES_SYSTEMES(13), GCU(15), MECANIQUE_AUTOMATIQUE(17);
	
	public final int fId;
	
	private TypesStudy(int id) {
		fId = id;
	}
	
	public static TypesStudy forInt(int id) {
        for (TypesStudy typeStudy : values()) {
            if (typeStudy.fId == id) {
                return typeStudy;
            }
        }
        throw new IllegalArgumentException("Invalid type of study id: " + id);
    }
}
