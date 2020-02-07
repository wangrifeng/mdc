package com.app.mdc.enums;

public enum InfuraInfo {

   MDC_CONTRACT_ADDRESS("0x53509548c0ce0be4bb88b85f4d2c37b2c5cd1546"),
    USDT_CONTRACT_ADDRESS("0xdac17f958d2ee523a2206206994597c13d831ec7"),
    RECHARGE_WITHDRAWAL("0x4f723F317194cDb24D407D25d967d1F99530c276"),
    ETH_FINNEY("1000"),
    MDC_ETH("1000000000000000000"),
    GAS_PRICE("18"),
    GAS_SIZE("100000"),
   INFURA_ADDRESS("https://ropsten.infura.io/v3/4098a0ceccd5421fa162fb549adea10a");






    private String desc;
    InfuraInfo(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public InfuraInfo setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
