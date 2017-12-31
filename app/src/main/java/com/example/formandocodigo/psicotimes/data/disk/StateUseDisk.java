package com.example.formandocodigo.psicotimes.data.disk;

import com.example.formandocodigo.psicotimes.entity.App;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StateUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public interface StateUseDisk {
    Integer putApplicationAll(ArrayList<App> applicationList);

    Integer putStateUserAll(ArrayList<StateUser> stateUserList);

    Integer putHistoricStateAll(ArrayList<HistoricState> historicStateList);

    List<StateUser> getStateUserAll();

    List<App> getAppAll();

    List<HistoricState> getHistoricStateAll();

    List<StateUse> findStateUseByIdAll(Integer id);

    List<StateUse> getStateUseAll();

    List<StateUse> getStateUseByDate();
}
