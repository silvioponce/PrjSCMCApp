package Entidades;

public class Comunidad {

    //region Variables Miembros
    private Integer _IdComunidad;
    private Integer IdComunidad;
    private String NomComunidad;
    private Integer IdMunicipio;

    public static final String TABLE_NAME = "Comunidades";
    //endregion

    //region Constructors
    public Comunidad() {

    }

    public Comunidad(Integer _idComunidad,  Integer idComunidad, String nomComunidad, Integer idMunicipio) {
        this._IdComunidad = _idComunidad;
        this.IdComunidad = idComunidad;
        this.NomComunidad = nomComunidad;
        this.IdMunicipio = idMunicipio;
    }

    //endregion

    //region Public Properties
    public Integer get_IdComunidad() {
        return _IdComunidad;
    }

    public void set_IdComunidad(Integer _idComunidad) {
        _IdComunidad = _idComunidad;
    }

    public Integer getIdComunidad() {
        return IdComunidad;
    }

    public void setIdComunidad(Integer idComunidad) {
        IdComunidad = idComunidad;
    }

    public String getNomComunidad() {
        return NomComunidad;
    }

    public void setNomComunidad(String nomComunidad) {
        NomComunidad = nomComunidad;
    }

    public Integer getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        IdMunicipio = idMunicipio;
    }

    //endregion

    @Override
    public String toString () {
        return NomComunidad;
    }

}

