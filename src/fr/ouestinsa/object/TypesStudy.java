package fr.ouestinsa.object;

import fr.ouestinsa.R;

public enum TypesStudy {
	ELECTRONIQUE(1, R.drawable.fa_plug), 
	SITE_WEB(2, R.drawable.fa_laptop), 
	LOGICIEL(4, R.drawable.fa_laptop), 
	INFORMATIQUE(6, R.drawable.fa_laptop), 
	TRADUCTION_TECHNIQUE(8, R.drawable.fa_pencil), 
	AUTOMATISATION_DES_SYSTEMES(13, R.drawable.fa_cogs), 
	GENIE_CIVIL_ET_URBANISME(15, R.drawable.fa_university), 
	MECANIQUE_AUTOMATIQUE(17, R.drawable.fa_cogs);
	
	private final int id;
	
	public final int ressource;
	
	private TypesStudy(int id, int ressource) {
		this.id = id;
		this.ressource = ressource;
	}
	
	public static TypesStudy forInt(int id) throws IllegalArgumentException {
        for (TypesStudy typeStudy : values()) {
            if (typeStudy.id == id) {
                return typeStudy;
            }
        }
        throw new IllegalArgumentException("Invalid type of study id: " + id);
    }
	
	public int getRessource() {
		return ressource;
	}
}
