PACKAGE org.apache.uima;

DECLARE T1, T2, T3, T4, T5;

CW{-> MARK(T3)} W+{-PARTOF(T1) -> MARK(T1,1,2)} PERIOD;
SW{-> MARK(T3)} W+{-PARTOF(T2), -PARTOF(T1) -> MARK(T2,1,2)} PERIOD;
W{AND(PARTOF(T1), PARTOF(T3)) -> MARK(T4)};
W{AND(PARTOF(T2), PARTOF(T3)) -> MARK(T5)};