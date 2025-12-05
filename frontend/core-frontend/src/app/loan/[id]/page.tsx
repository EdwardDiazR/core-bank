"use client";

import { useRouter } from "next/navigation";
import React, { useEffect, useState } from "react";
import * as _loanService from "@/services/loanService";
import { Loan } from "@/models/Loan";
import styles from "../page.module.css";
import { formatNumber, formatNumberWithCurrency } from "@/utils/numberFormatter";
import { CustomInput } from "../components/CustomInput";

export default function LoanDetail({ params }: { params: Promise<{ id: string }> }) {
  const router = useRouter();
  const { id } = React.use(params);
  const [loan, setLoan] = useState<Loan>();

  useEffect(() => {
    _loanService.getLoanByNumber(id).then((r) => {
      setLoan(r.data);
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
        {loan && (
          <div className={styles.loanInfoGrid}>
            <div className={styles.infoItem}>
              <span>No. Prestamo</span>
              <CustomInput value={loan.productNumber} />
            </div>
            <div className={styles.infoItem}>
              <span>Estatus</span>
              <CustomInput value={loan.status} />
            </div>
            <div className={styles.infoItem}>
              <span>Tipo</span>
              <CustomInput value={loan.type} />
            </div>
            <div className={styles.infoItem}>
              <span>Monto del prestamo</span>
              <CustomInput value={formatNumberWithCurrency(loan.principalAmount, loan.currency)} />
            </div>
            <div className={styles.infoItem}>
              <span>Plazo (Meses)</span>
              <CustomInput value={loan.termInMonths.toString()} />
            </div>
            <div className={styles.infoItem}>
              <span>Balance de capital</span>
              <CustomInput value={formatNumberWithCurrency(loan.outstandingPrincipalAmount, loan.currency)} />
            </div>
            <div className={styles.infoItem}>
              <span>Balance de interes</span>
              <CustomInput value={formatNumberWithCurrency(loan.interestBalance, loan.currency)} />
            </div>
            <div className={styles.infoItem}>
              <span>Monto cuota</span>
              <CustomInput value={formatNumberWithCurrency(loan.installmentAmount, loan.currency)} />
            </div>
            <div className={styles.infoItem}>
              <span>Proxima fecha de pago</span>
              <CustomInput value={loan.nextPaymentDate ? loan.nextPaymentDate : ""} />
            </div>

            <div className={styles.infoItem}>
              <span>Fecha ultimo pago</span>
              <CustomInput
                value={
                  loan.lastPaymentDate
                    ? new Intl.DateTimeFormat("es-DO", { dateStyle: "short" }).format(new Date(loan.lastPaymentDate))
                    : ""
                }
              />
            </div>
          
              <div className={styles.infoItem}>
              <span>Tasa de interes</span>
              <CustomInput value={loan.interestRate + "%"} />
            </div>
             <div className={styles.infoItem}>
              <span>Fecha vencimiento</span>
              <CustomInput value={  loan.dueDate
                    ? new Intl.DateTimeFormat("es-DO", { dateStyle: "short" }).format(new Date(loan.dueDate))
                    : ""} />
            </div>
            <div className={styles.infoItem}>
              <span>Disponible para desembolso</span>
              <CustomInput value={formatNumberWithCurrency(loan.availableAmountForDisbursement, loan.currency)} />
            </div>

            

            <div className={styles.infoItem}>
              <span>Fecha ultima revision de tasa</span>
              <CustomInput
                value={
                  loan.lastInterestRateReviewDate
                    ? new Intl.DateTimeFormat("es-DO", { dateStyle: "short" }).format(
                        new Date(loan.lastInterestRateReviewDate)
                      )
                    : ""
                }
              />
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
