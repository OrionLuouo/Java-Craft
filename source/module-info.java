/**
 * Project: Craft<p>
 * Author: OrionLuouo<p>
 * Version: 1.0.0 Alpha<p>
 * <p>
 * This is a utility library contains a mess of various classes.
 */
/*
 * Package Information :<p>
 *      OrionLuouo.Craft.data {<p>
 *          "Things involved in data,<p>
 *          like data storage, management, transmission...";<p>
 *      }<p>
 *
 *      OrionLuouo.Craft.logic {<p>
 *          "To optimize the logic of a Java program that uses Craft library.";<p>

 *          .input {<p>
 *              "Classes under this package is mainly used for logically accepting and parsing the input.<p>
 *              They don't affect the logic of programs with the input content,<p>
 *              but you can use them to build complex logic of parsing input more easily,<p>
 *              like construct a system for structured documents' parsing.";<p>
 *          }<p>

 *          .control {<p>
 *              "Classes in this package are like a control of television,<p>
 *              it accepts commands inputted from I/O access,<p>
 *              and inject the logic into the program.";<p>
 *          }<p>
 *      }<p>
 * }
 */
module OrionLuouo.Craft {
    requires java.desktop;
    requires java.sql;
    requires java.xml.crypto;

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
    exports OrionLuouo.Craft.logic.control.COS;
    exports OrionLuouo.Craft.logic.input.SDP;
}