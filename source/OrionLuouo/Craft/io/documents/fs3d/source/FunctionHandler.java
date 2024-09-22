package OrionLuouo.Craft.io.documents.fs3d.source;

import OrionLuouo.Craft.io.documents.fs3d.FS3DObject;
import OrionLuouo.Craft.io.documents.fs3d.FS3DType;

public record FunctionHandler(Handler[] argumentSources , FunctionVariable function) implements Handler {
    @Override
    public FS3DType getType() {
        return function.getType();
    }

    @Override
    public FS3DObject getValue() {
        if (argumentSources == null) {
            return function.functionInstance.invoke(null);
        }
        FS3DObject[] arguments = new FS3DObject[argumentSources.length];
        for (int i = 0; i < argumentSources.length; i++) {
            arguments[i] = argumentSources[i].getValue();
        }
        return function.functionInstance.invoke(arguments);
    }
}