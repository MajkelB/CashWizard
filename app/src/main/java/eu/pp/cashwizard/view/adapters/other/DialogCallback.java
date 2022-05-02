package eu.pp.cashwizard.view.adapters.other;

import eu.pp.cashwizard.dict.AnswerType;

public interface DialogCallback {

        void answer(int communicationId, AnswerType answerType, String... additionalData );

}
