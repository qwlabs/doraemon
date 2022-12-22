package com.qwlabs.lang;

public enum YesOrNo {
    YES {
        @Override
        public boolean booleanValue() {
            return true;
        }

        @Override
        public String shortName() {
            return "Y";
        }
    },
    NO {
        @Override
        public boolean booleanValue() {
            return false;
        }

        @Override
        public String shortName() {
            return "N";
        }
    };

    public abstract boolean booleanValue();

    public abstract String shortName();

    public boolean isYes() {
        return YES == this;
    }

    public boolean isNo() {
        return NO == this;
    }

    public YesOrNo and(YesOrNo that) {
        return of(this.booleanValue() && that.booleanValue());
    }

    public YesOrNo or(YesOrNo that) {
        return of(this.booleanValue() || that.booleanValue());
    }

    public static YesOrNo of(boolean yesOrNo) {
        return yesOrNo ? YES : NO;
    }
}
