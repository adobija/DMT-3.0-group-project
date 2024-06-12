package com.dmt.bankingapp;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BcryptPasswordGeneratorTests {

    @ParameterizedTest
    @ValueSource(strings = {"eZOrELq5l1rCpwK",
            "jqYAoNjaMUxNziL",
            "VI2BH6dLX0pr3ed",
            "7CgzLxNRPglbuY5",
            "IVJHpm1pJH8XLHK",
            "hp2NraVuW05ufAH",
            "pk8pHg73alhlqZv",
            "yqH9RyG7SJPA9VA",
            "0PjeDUv946PTjFv",
            "1FK1DWTmf1g7Mw5",
            "ddw8CsXLGM97jBo",
            "2PMPFfzL1aYMk5N",
            "Hk4FSrKlAUIctmZ",
            "DNCVgGWAWtuyx2C",
            "56JxlH6HaSzsu4a",
            "Vgv6KHPbqlW25Vc",
            "4YRBmsQeVU35Nuz",
            "dA2Jh2CIqdlxBxZ",
            "wExGgOTwPLeLC80",
            "SkpGnPgM9BkKlVv",
            "yVMup7KWqhVUTmk",
            "KNYaSgMaNdX8pL8",
            "WNQbvkf1oeasH5g",
            "05lNSKUBM5acmq2",
            "4rZA618eyhNWoe4",
            "YnbhjyG5vK83rcT",
            "D2esPXNA6k4QLQd",
            "HC0oJPNCf70Qjry",
            "dyPlJsRlbc7BYVU",
            "qbYd1SnRyV0mRmC",
            "E2tkuFO7BYIXNcU",
            "XRoRD41lGfe0Bkd",
            "yp1dIUOkQjhKPP9",
            "qbv15hod6KpYQWA",
            "CuhiiLDOdpX0XH8",
            "nbIrt90vlRuw0rl",
            "Dyn06uEbco1X7Aa",
            "Ml4H53Rp5wzx0u0",
            "2Ivz8NftZmbQUs0",
            "WuqnwZg2BY2mDQJ",
            "CGjtZkRbTR2dg1q",
            "18Y8hRIiLwA91PW",
            "N1d47iHEUe6Ntb8",
            "RRMJye2SLg5wfTC",
            "8DNOAW3gE1XGmEM",
            "m9MOhmqpIHWXe4P",
            "TvlrexJ1Vs3z25q",
            "gf69HAwfvP1ubP3",
            "tkpukrseDZ0NsJe",
            "1tNCit6jmNUD99H",
            "FiTOaBIKi7aVg3M",
            "mN4VqohXbiQR0qy",
            "QUeXr74a2xsqmQP",
            "40s6eN3I91A7tur",
            "2GNt2Fn5ktsbUL5",
            "XkLPwR7RHQGz8jE",
            "ifPK2zzT7NQAnZX",
            "CzAM716lcKhgL4f",
            "4zfb6aZXMXoElst",
            "UDDmY3toBrNcHYb",
            "O4CtKaO0QL27MqZ",
            "EdlBpkWB3AZrVHD",
            "AkRjSwClf63Swx5",
            "vo9j5zwIVDKEdrT",
            "vSspY06wfLgDEp2",
            "xjT7Z9raGn1AdBt",
            "bGH0TveyMRPetAa",
            "SeY6Zk0AGdZKYp3",
            "gibP7VcvKjw6M01",
            "KZx1v0iXKEGfrsp",
            "aja1RFIbNVWxzhG",
            "H3C2PFtnhE57j2H",
            "8WMFEjvyjO5czpW",
            "kTXDu9ud4k6dTdo",
            "FkGKwjQJ1omm16h",
            "XsqdkmuFImlUpUE",
            "gYYmDQCe9jvOBrO",
            "bSRs6KHrIqmeDlX",
            "9bbtXzhKVGZaSGB",
            "AjSwau38cOfXuzx",
            "i13i0utKa7MxrEO",
            "xLJx58VgHdusUK3",
            "y3VXH5kAg3t2I2n",
            "KMYiJHjqfdIU1JE",
            "hQxgmGoICtXj6Iw",
            "GMdYjIaCYkYFtuq",
            "wkyBc5nM6mliN8Y",
            "9dhQvH4CJdwdTEL",
            "wyVA5zmscTtnIlP",
            "0LFXC0MrqgiZiuS",
            "d8E5JS6mhTJ08BQ",
            "g63kdXOw19njHjj",
            "xFia2DmLhrDs7k3",
            "vxR3mLLYgPtAI8D",
            "ow8XF9yBEZg7BMq",
            "YwJP3rqs0cEMDTC",
            "RcbpGabnEs0O99O",
            "QdWHpQ9abENDMRN",
            "EcRgwuOC8RDll2Q",
            "kZEqMhSUj0J2rEp"})
    public void checkForValidBcryptPass(String plainTextPassword){
        //arrange
        //act
        String hashPassword = BCrypt.hashpw(plainTextPassword,BCrypt.gensalt(10));
        //assert
        assertTrue(BCrypt.checkpw(plainTextPassword, hashPassword));
    }
}