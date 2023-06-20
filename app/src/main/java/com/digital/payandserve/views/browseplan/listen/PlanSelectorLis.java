package com.digital.payandserve.views.browseplan.listen;

import com.digital.payandserve.views.browseplan.model.DataItem;

public interface PlanSelectorLis {
        void onButtonSelect(int p, DataItem model);

        void onImgExpand(int p, DataItem model);
    }