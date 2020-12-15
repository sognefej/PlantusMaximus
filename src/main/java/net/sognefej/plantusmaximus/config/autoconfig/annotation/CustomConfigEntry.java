package net.sognefej.plantusmaximus.config.autoconfig.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class CustomConfigEntry {
    private CustomConfigEntry() {
    }

    public static class Gui {
        private Gui() {
        }

        /**
         * Applies to strings.
         * Adds GUI key code entry to menu
         */
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.FIELD)
        public @interface KeyCodeEntry {
        }
    }
}
