package com.sbg.bdd.android.screenplay;

import com.sbg.bdd.screenplay.core.Actor;
import com.sbg.bdd.screenplay.core.Task;
import com.sbg.bdd.screenplay.core.annotations.ProducesAttachment;
import com.sbg.bdd.screenplay.core.annotations.Step;

import static com.sbg.bdd.screenplay.core.util.NameConverter.humanize;

public abstract class AndroidTask implements Task {
    @ProducesAttachment(mimeType = "image/png")
    byte[] screenshot;
    private String description;

    public AndroidTask() {
        this(humanize(Thread.currentThread().getStackTrace()[4].getMethodName()));
    }
    public AndroidTask(String description) {
        this.description = description;
    }

    @Override
    @Step("#description")
    public <T extends Actor> T performAs(T actor) {
        OperateTheAndroidApp operateTheAndroidApp = actor.usingAbilityTo(OperateTheAndroidApp.class);
        T perform = perform(actor, operateTheAndroidApp);
        screenshot = operateTheAndroidApp.takeScreenShot();
        return perform;
    }

    protected abstract <T extends Actor> T perform(T actor, OperateTheAndroidApp operateTheAndroidApp);
}
