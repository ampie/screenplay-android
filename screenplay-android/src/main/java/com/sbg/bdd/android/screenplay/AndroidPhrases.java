package com.sbg.bdd.android.screenplay;

import android.support.test.espresso.ViewInteraction;
import android.view.View;

import com.sbg.bdd.android.screenplay.ViewInteractionConsequence;
import com.sbg.bdd.screenplay.core.Question;

import org.hamcrest.Matcher;

public class AndroidPhrases {
    public static ViewInteractionConsequence see(Question<ViewInteraction> interaction, Matcher<View> matcher) {
        return new ViewInteractionConsequence(interaction, matcher);
    }

    public static ViewInteractionConsequence see(String subject, Question<ViewInteraction> interaction, Matcher<View> matcher) {
        return new ViewInteractionConsequence(subject, interaction, matcher);
    }
}
