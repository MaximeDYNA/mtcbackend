package com.catis.objectTemporaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardData {
    private int visitNumber;
    private double ca;
    private double tax;
    private List<Double> caGraph;
    private List<OrganisationTopDTO> organisationTopDTOS;
    private List<DaschBoardLogDTO> daschBoardLogDTOS;
}
