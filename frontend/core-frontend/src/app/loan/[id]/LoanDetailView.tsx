// app/loan/[id]/LoanDetailView.tsx
"use client";

import styles from "../page.module.css";
import { LoanDto } from "@/models/LoanDto";
import { formatNumberWithCurrency } from "@/utils/numberFormatter";
import { CustomInput } from "../components/CustomInput";
import { AmortizationTableView } from "../components/AmortizationTableView";

type Props = {
  loan: LoanDto;
};

export default function LoanDetailView({ loan }: Props) {

  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <div className={styles.header}>
          <span>
            <h3>Detalle del préstamo</h3>
          </span>
          <button>Cerrar</button>
        </div>

        <div className={styles.loanInfoGrid}>
          <Info label="No. Préstamo" value={loan.number} />
          <Info label="Estatus" value={loan.status} />
          <Info label="Tipo" value="TIPO PRESTAMO" />
          <Info label="Monto del préstamo" value={formatNumberWithCurrency(loan.principalAmount, loan.currency)} />
          <Info label="Plazo (Meses)" value={loan.termInMonths.toString()} />
          <Info
            label="Balance de capital"
            value={formatNumberWithCurrency(loan.outstandingPrincipalBalance, loan.currency)}
          />
          <Info label="Balance de interés" value={formatNumberWithCurrency(loan.interestBalance, loan.currency)} />
          <Info label="Monto cuota" value={formatNumberWithCurrency(loan.installmentAmount, loan.currency)} />
          <Info label="Próxima fecha de pago" value={formatDate(loan.nextPaymentDate?.toDateString())} />
          <Info label="Fecha último pago" value={formatDate(loan.lastPaymentDate?.toDateString())} />
          <Info label="Tasa de interés" value={`${loan.interestRate}%`} />
          <Info label="Fecha vencimiento" value={formatDate(loan.dueDate.toString())} />
          <Info
            label="Disponible para desembolso"
            value={formatNumberWithCurrency(loan.availableAmountForDisbursement, loan.currency)}
          />
          <Info label="Última revisión de tasa" value={formatDate(loan.lastInterestRateReviewDate?.toString())} />
        </div>
      </div>
      <div>
        
      </div>
      <AmortizationTableView amortizationTable={loan.amortizationTable} currency={loan.currency} />
    </div>
  );
}

function Info({ label, value }: { label: string; value?: string }) {
  return (
    <div className={styles.infoItem}>
      <span>{label}</span>
      <CustomInput value={value ?? ""} />
    </div>
  );
}

function formatDate(date?: string) {
  if (!date) return "";
  return new Intl.DateTimeFormat("es-DO", { dateStyle: "short" }).format(new Date(date));
}
