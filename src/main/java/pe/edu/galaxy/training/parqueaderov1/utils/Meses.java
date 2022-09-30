package pe.edu.galaxy.training.parqueaderov1.utils;

public enum Meses {
    ENERO("2022-01"),
    FEBRERO("2022-02"),
    MARZO("2022-03"),
    ABRIL("2022-04"),
    MAYO("2022-05"),
    JUNIO("2022-06"),
    JULIO("2022-07"),
    AGOSTO("2022-08"),
    SEPTIEMBRE("2022-09"),
    OCTUBRE("2022-10"),
    NOVIEMBRE("2022-11"),
    DICIEMBRE("2022-12");
    private final String value;

    Meses(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
