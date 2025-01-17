package OrionLuouo.Craft.logic.input.SDP;

public record Operation(Operator operator , int recordIndex , int unitIndex) {
    enum Operator {
        ROLLBACK , CONTINUE , MISMATCH , MATCH , RECORD , UPDATE
    }
}
