package com.general.mediaplayer.TwoInOne;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/17/14
 * Time: 9:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonData {

    // playing video time from no interaction (in seconds)
    public static final int TIME_PLAYVIDEO = 60; //60 seconds
    public static final int VIDEO_LOOPING = 1;
    public static final int START_SERVER = 1;
    public static final int USE_SERIAL = 1;
    public static final int FIXED_SIZE = 0;
    public static final int START_TETHERING = 0;


    //enum for transform
    public static final int TRANSFORM_ALPHA_FADE = 0;
    public static final int TRANSFORM_TRANSFORM_SLIDE = 1;
    public static final int TRANSFORM_ALPHA_SLIDE = 2;

    public static final int transformAnimation = TRANSFORM_ALPHA_SLIDE; //trans_0

    public static final int PAGE_NONE = 0;
    public static final int PAGE_BASIC = 1;
    public static final int PAGE_ADVANCED = 2;

    public static int backFrom = PAGE_NONE;

    // enum for budget
    public static final int PRICE_FIRST = 0;
    public static final int PRICE_SECOND = 1;
    public static final int PRICE_THIRD = 2;
    public static final int PRICE_FORTH = 3;

    // enum for basic
    public static final int BASIC_NONE = 0;
    public static final int BASIC_FIRST = 0;
    public static final int BASIC_SECOND = 1;
    public static final int BASIC_THIRD = 2;
    public static final int BASIC_FORTH = 3;

    // enum for advanced
    public static final int ADVANCED_NONE = 0;
    public static final int ADVANCED_FIRST = 0;
    public static final int ADVANCED_SECOND = 1;
    public static final int ADVANCED_THIRD = 2;
    public static final int ADVANCED_FORTH = 3;

    // enum for result
    public static final int RESULT_DETACH = 1; // ASUS
    public static final int RESULT_FLIP = 2; // Lenevo
    public static final int RESULT_SPIN = 3; // Dell XPS
    public static final int RESULT_SLIDE = 4; // Sony

    public static int getDefaultSelectedPrice() {
        //return PRICE_FIRST;
        return PRICE_SECOND;
    }

    public static int getDefaultSelectedBasic() {
        return BASIC_NONE;
    }

    public static int getDefaultSelectedAdvanced() {
        return ADVANCED_NONE;
    }

    static class SuitableOption {
        public int _price;
        public int _selectableBasic;
        public int _selectableAdvanced;
        public int _result;

        public SuitableOption()
        {
            /*
            _price = PRICE_FIRST;
            _selectableBasic = ((1 << BASIC_FIRST) | (1 << BASIC_SECOND) | (1 << BASIC_THIRD));
            _selectableAdvanced = ((1 << ADVANCED_FIRST) | (1 << ADVANCED_SECOND) | (1 << ADVANCED_THIRD) | (1 << ADVANCED_FORTH));
            _result = RESULT_SLIDE;
            */

            _price = PRICE_SECOND;
            _selectableBasic = ((1 << BASIC_FIRST) | (1 << BASIC_SECOND) | (1 << BASIC_THIRD));
            _selectableAdvanced = ((1 << ADVANCED_FIRST) | (1 << ADVANCED_SECOND));
            _result = RESULT_SPIN;
        }

        public boolean isBasicSuitable(int basic)
        {
            if ((_selectableBasic &  basic) == basic)
                return true;
            return false;
        }

        public boolean isAdvancedSuitable(int advanced)
        {
            if ((_selectableAdvanced & advanced) == advanced)
                return true;
            return false;
        }

        public boolean isBasicSuitableWithOne(int basicOption)
        {
            int basic = (1 << basicOption);
            return isBasicSuitable(basic);
        }

        public boolean isAdvancedSuitableWithOne(int advancedOption)
        {
            int advanced = (1 << advancedOption);
            return isAdvancedSuitable(advanced);
        }
    }

    private static SuitableOption _optionSlide;
    private static SuitableOption _optionSpin;
    private static SuitableOption _optionFlip;
    private static SuitableOption _optionDetach;

    static {
        _optionSlide = new SuitableOption();
        _optionSpin = new SuitableOption();
        _optionFlip = new SuitableOption();
        _optionDetach = new SuitableOption();

        initialize();
    }

    // initialize Suitable option and ...
    public static void initialize()
    {
        _optionSlide._price = PRICE_FIRST;
        _optionSlide._selectableBasic = ((1 << BASIC_FIRST) | (1 << BASIC_SECOND) | (1 << BASIC_THIRD));
        _optionSlide._selectableAdvanced = ((1 << ADVANCED_FIRST) | (1 << ADVANCED_SECOND) | (1 << ADVANCED_THIRD) | (1 << ADVANCED_FORTH));
        _optionSlide._result = RESULT_SLIDE;


        _optionSpin._price = PRICE_SECOND;
        _optionSpin._selectableBasic = ((1 << BASIC_FIRST) | (1 << BASIC_SECOND) | (1 << BASIC_THIRD));
        _optionSpin._selectableAdvanced = ((1 << ADVANCED_FIRST) | (1 << ADVANCED_SECOND));
        _optionSpin._result = RESULT_SPIN;


        _optionFlip._price = PRICE_THIRD;
        _optionFlip._selectableBasic = ((1 << BASIC_FIRST) | (1 << BASIC_SECOND));
        _optionFlip._selectableAdvanced = ((1 << ADVANCED_FIRST) | (1 << ADVANCED_SECOND));
        _optionFlip._result = RESULT_FLIP;


        _optionDetach._price = PRICE_FORTH;
        _optionDetach._selectableBasic = ((1 << BASIC_FIRST) | (1 << BASIC_SECOND) | (1 << BASIC_FORTH));
        _optionDetach._selectableAdvanced = ((1 << ADVANCED_SECOND));
        _optionDetach._result = RESULT_DETACH;
    }


    // get Suitable Option with selected price
    public static SuitableOption getSuitableOption(int priceOption)
    {
        switch (priceOption)
        {
            case PRICE_FIRST:
                return _optionSlide;
            case PRICE_SECOND:
                return _optionSpin;
            case PRICE_THIRD:
                return _optionFlip;
            case PRICE_FORTH:
                return _optionDetach;
            default:
                return _optionSlide;
        }
    }
}
