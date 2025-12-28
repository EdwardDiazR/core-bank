"use client";

import { useRouter } from "next/navigation";
import React, { useEffect, useState } from "react";
import * as _loanService from "@/services/loanService";
import { Loan } from "@/models/Loan";
import styles from "../page.module.css";
import { formatNumber, formatNumberWithCurrency } from "@/utils/numberFormatter";
import { CustomInput } from "../components/CustomInput";
import { LoanDto } from "@/models/LoanDto";

export default function LoanDetail({ params }: { params: Promise<{ id: string }> }) {
  const router = useRouter();
  const { id } = React.use(params);
  const [loan, setLoan] = useState<LoanDto | null>(null);

  useEffect(() => {
    _loanService.getLoanByNumber(id).then((r) => {
      setLoan(r.data.data);
      console.log(r.data.data);
    });
  }, [id]);

  return (
    <div className={styles.container}>
      
      <div className={styles.card}>
        <div className={styles.header}>
          <span>
            <i></i>
            <h3>Detalle del prestamo</h3>
          </span>
          <button>Cerrar</button>
        </div>

        <div className={styles.loanInfoGrid}>
          <div className={styles.infoItem}>
            <span>No. Prestamo</span>
            <CustomInput value={loan ? loan.financialProduct.productNumber : ""} />
          </div>
          <div className={styles.infoItem}>
            <span>Estatus</span>
            <CustomInput value={loan ? loan.status : ""} />
          </div>
          <div className={styles.infoItem}>
            <span>Tipo</span>
            <CustomInput value={loan ? loan.financialProduct.productType : ""} />
          </div>
          <div className={styles.infoItem}>
            <span>Monto del prestamo</span>
            <CustomInput value={loan ? formatNumberWithCurrency(loan.principalAmount, loan.currency) : ""} />
          </div>
          <div className={styles.infoItem}>
            <span>Plazo (Meses)</span>
            <CustomInput value={loan ? loan.termInMonths.toString() : ""} />
          </div>
          <div className={styles.infoItem}>
            <span>Balance de capital</span>
            <CustomInput value={loan ? formatNumberWithCurrency(loan.outstandingPrincipalBalance, loan.currency) : ""} />
          </div>
          <div className={styles.infoItem}>
            <span>Balance de interes</span>
            <CustomInput value={loan ? formatNumberWithCurrency(loan.interestBalance, loan.currency) : ""} />
          </div>
          <div className={styles.infoItem}>
            <span>Monto cuota</span>
            <CustomInput value={loan ? formatNumberWithCurrency(loan.installmentAmount, loan.currency) : ""} />
          </div>
          <div className={styles.infoItem}>
            <span>Proxima fecha de pago</span>
            <CustomInput value={
                loan?.nextPaymentDate ? 
                   new Intl.DateTimeFormat("es-DO", { dateStyle: "short" }).format(new Date(loan.nextPaymentDate))
                  : ""
              } />
          </div>

          <div className={styles.infoItem}>
            <span>Fecha ultimo pago</span>
            <CustomInput
              value={
                loan?.lastPaymentDate
                  ? new Intl.DateTimeFormat("es-DO", { dateStyle: "short" }).format(new Date(loan.lastPaymentDate))
                  : ""
              }
            />
          </div>

          <div className={styles.infoItem}>
            <span>Tasa de interes</span>
            <CustomInput value={loan ? loan.interestRate + "%" : ""} />
          </div>
          <div className={styles.infoItem}>
            <span>Fecha vencimiento</span>
            <CustomInput
              value={
                loan && loan.dueDate
                  ? new Intl.DateTimeFormat("es-DO", { dateStyle: "short" }).format(new Date(loan.dueDate))
                  : ""
              }
            />
          </div>
          <div className={styles.infoItem}>
            <span>Disponible para desembolso</span>
            <CustomInput
              value={loan ? formatNumberWithCurrency(loan.availableAmountForDisbursement, loan.currency) : ""}
            />
          </div>

          <div className={styles.infoItem}>
            <span>Fecha ultima revision de tasa</span>
            <CustomInput
              value={
                loan 
                  ? new Intl.DateTimeFormat("es-DO", { dateStyle: "short" }).format(
                      new Date(loan.lastInterestRateReviewDate)
                    )
                  : ""
              }
            />
          </div>
        </div>
      </div>
    </div>
  );
}
