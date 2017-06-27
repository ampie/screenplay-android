package com.sbg.bdd.android.screenplay;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.view.View;

import com.sbg.bdd.screenplay.core.Ability;
import com.sbg.bdd.screenplay.core.Actor;
import com.sbg.bdd.screenplay.core.ActorOnStage;
import com.sbg.bdd.screenplay.core.actors.OnStage;
import com.sbg.bdd.screenplay.wiremock.CorrelationPath;
import com.sbg.bdd.wiremock.scoped.integration.BaseWireMockCorrelationState;
import com.sbg.bdd.wiremock.scoped.integration.DependencyInjectionAdaptorFactory;
import com.sbg.bdd.wiremock.scoped.integration.DependencyInjectorAdaptor;
import com.sbg.bdd.wiremock.scoped.integration.EndPointRegistry;
import com.sbg.bdd.wiremock.scoped.integration.PropertiesEndpointRegistry;
import com.sbg.bdd.wiremock.scoped.integration.WireMockCorrelationState;

import org.hamcrest.Matcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import static android.support.test.runner.lifecycle.Stage.RESUMED;

public class OperateTheAndroidApp implements Ability {
    private static Instrumentation instrumentation;
    private WireMockCorrelationState wireMockCorrelationState = new BaseWireMockCorrelationState();
    DependencyInjectorAdaptor adaptor = new DependencyInjectorAdaptor() {
        @Override
        public WireMockCorrelationState getCurrentCorrelationState() {
            return wireMockCorrelationState;
        }

        @Override
        public EndPointRegistry getEndpointRegistry() {
            return new PropertiesEndpointRegistry(new Properties());
        }
    };

    private Actor actor;

    public ViewInteraction onView(Matcher<View> viewMatcher) {
        DependencyInjectionAdaptorFactory.useAdapter(adaptor);
        ActorOnStage actorOnStage = OnStage.callActorToStage(actor);
        String correlationPath = CorrelationPath.of(actorOnStage);
        if (wireMockCorrelationState.getCorrelationPath() ==null || !wireMockCorrelationState.getCorrelationPath().equals(correlationPath)) {
            wireMockCorrelationState.clear();
            wireMockCorrelationState.set(correlationPath, true);
        }
        return Espresso.onView(viewMatcher);
    }

    @Override
    public <T extends Ability> T asActor(Actor actor) {
        this.actor = actor;
        return (T) this;
    }

    public static void useInstrumentation(Instrumentation instrumentation) {
        OperateTheAndroidApp.instrumentation = instrumentation;
    }

    public byte[] takeScreenShot() {
        View screenView = getCurrentActivity().getWindow().getDecorView().getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public Activity getCurrentActivity() {
        final Activity[] currentActivity = new Activity[1];
        instrumentation.runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities =
                        ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity[0] = resumedActivities.iterator().next();
                }
            }
        });
        return currentActivity[0];
    }

}
