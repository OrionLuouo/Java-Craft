module OrionLuouo.Craft {
    requires java.desktop;
    requires java.sql;

    exports OrionLuouo.Craft.data;
    exports OrionLuouo.Craft.data.container;
    exports OrionLuouo.Craft.data.container.map;
    exports OrionLuouo.Craft.data.container.collection;
    exports OrionLuouo.Craft.data.container.collection.sequence;
    exports OrionLuouo.Craft.data.math;
    exports OrionLuouo.Craft.io;
    exports OrionLuouo.Craft.io.documents.xml;
    exports OrionLuouo.Craft.gui.animation;
    exports OrionLuouo.Craft.gui.complex;
    exports OrionLuouo.Craft.gui.component.window;
    exports OrionLuouo.Craft.gui.component.image;
    exports OrionLuouo.Craft.gui.component.panel;
    exports OrionLuouo.Craft.gui.component.text;
    exports OrionLuouo.Craft.gui.graphics;
    exports OrionLuouo.Craft.gui.kits;
    exports OrionLuouo.Craft.gui.listener.keyListener;
    exports OrionLuouo.Craft.gui.listener.mouseListener;
    exports OrionLuouo.Craft.system.annotation;
    exports OrionLuouo.Craft.system.reflect;
    exports OrionLuouo.Craft.system.thread;
    exports OrionLuouo.Craft.system.utilities;
}