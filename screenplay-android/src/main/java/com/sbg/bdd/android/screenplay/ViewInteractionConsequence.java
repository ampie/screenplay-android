package com.sbg.bdd.android.screenplay;

import android.support.test.espresso.ViewInteraction;
import android.view.View;

import com.sbg.bdd.screenplay.core.Actor;
import com.sbg.bdd.screenplay.core.BaseConsequence;
import com.sbg.bdd.screenplay.core.Question;
import com.sbg.bdd.screenplay.core.QuestionSubject;
import com.sbg.bdd.screenplay.core.annotations.ProducesAttachment;
import com.sbg.bdd.screenplay.core.util.StripRedundantTerms;

import org.hamcrest.Matcher;

import static android.support.test.espresso.assertion.ViewAssertions.matches;

public class ViewInteractionConsequence extends BaseConsequence<ViewInteraction> {
    private final Question<ViewInteraction> viewInteraction;
    private final Matcher<View> viewMatcher;
    private String subject;
    @ProducesAttachment
    private byte[] screenshot;
    public ViewInteractionConsequence(String subject, Question<ViewInteraction> viewInteraction, Matcher<View> viewMatcher) {
        this(viewInteraction,viewMatcher);
        this.subject=subject;
    }

    public ViewInteractionConsequence(Question<ViewInteraction> viewInteraction, Matcher<View> viewMatcher) {
        this.viewInteraction = viewInteraction;
        this.viewMatcher = viewMatcher;
        this.subject = QuestionSubject.fromClass(viewInteraction.getClass()).andQuestion(viewInteraction).subject();
    }

    @Override
    public void evaluateFor(Actor actor) {
        ViewInteraction viewInteraction = this.viewInteraction.answeredBy(actor);
        viewInteraction.check(matches(viewMatcher));
//        screenshot= actor.usingAbilityTo(OperateTheAndroidApp.class).takeScreenShot();

    }

    @Override
    public String toString() {
        String template = explanation.or("see %s  %s");
        String expectedExpression = StripRedundantTerms.from(viewMatcher.toString());
        return addRecordedInputValuesTo(String.format(template, subjectText.or(subject), expectedExpression));
    }
    @Override
    public Question<ViewInteraction> getQuestion() {
        return viewInteraction;
    }
}
