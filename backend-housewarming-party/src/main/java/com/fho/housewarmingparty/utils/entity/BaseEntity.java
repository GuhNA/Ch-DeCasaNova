package com.fho.housewarmingparty.utils.entity;

import java.io.Serializable;

public interface BaseEntity<I extends Serializable> {

    I getId();
}
