package OrionLuouo.Craft.io.documents.fs3d.source;

public abstract class AreaLayer extends StateLayer {
    AreaLayer(DocumentStatement statement) {
        super(statement);
    }

    public abstract void reload();
    public abstract void logout();
}

class TypeAreaLayer extends AreaLayer {
    TypeStatement type;

    TypeAreaLayer(DocumentStatement statement , TypeStatement type) {
        super(statement);
        this.type = type;
    }

    @Override
    public void reload() {

    }

    @Override
    public void logout() {

    }
}
