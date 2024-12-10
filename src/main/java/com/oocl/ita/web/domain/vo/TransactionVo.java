package com.oocl.ita.web.domain.vo;

import com.oocl.ita.web.domain.po.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class TransactionVo {
    private Integer id;
    private Double amountInUsd;
    private Double amountInLocalCurrency;
    private String localCurrency;
    private String concertName;
    private String startTime;
    private String concertClassName;
    private List<TicketVo> ticketVos;

    public static TransactionVo toVo(Transaction transaction) {
        TransactionVo transactionVo = new TransactionVo();
        transactionVo.setId(transaction.getId());
        transactionVo.setAmountInUsd(transaction.getAmountInUsd());
        transactionVo.setAmountInLocalCurrency(transaction.getAmountInLocalCurrency());
        transactionVo.setLocalCurrency(transaction.getLocalCurrency());
        return transactionVo;
    }

    public static TransactionVo toVo(TransactionVo transactionVo, Transaction transaction) {
        transactionVo.setId(transaction.getId());
        transactionVo.setAmountInUsd(transaction.getAmountInUsd());
        transactionVo.setAmountInLocalCurrency(transaction.getAmountInLocalCurrency());
        transactionVo.setLocalCurrency(transaction.getLocalCurrency());
        return transactionVo;
    }
}
