package com.example.formandocodigo.psicotimes.domain;

import com.example.formandocodigo.psicotimes.entity.StateUse;

import java.util.List;

/**
 * Created by FormandoCodigo on 15/12/2017.
 */

public interface StateUseCase {
    List<StateUse> findStateUseByIdAll(Integer id);

    List<StateUse> getStateUseAll();

    List<StateUse> getStateUseByDate();
}
