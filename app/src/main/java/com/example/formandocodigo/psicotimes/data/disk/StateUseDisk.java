package com.example.formandocodigo.psicotimes.data.disk;

import com.example.formandocodigo.psicotimes.entity.ApplicationEntity;
import com.example.formandocodigo.psicotimes.entity.StateUser;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public interface StateUseDisk {
    void putApplication(ApplicationEntity applicationEntity);

    void putApplicationAll(ArrayList<ApplicationEntity> applicationList);

    Integer putStateUserAll(ArrayList<StateUser> stateUserList);
}
