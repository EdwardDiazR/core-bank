"use client"

import styles from "./page.module.css";
import { LoanDto } from "@/models/LoanDto";
import { formatNumberWithCurrency } from "@/utils/numberFormatter";
import { CustomInput } from "@/app/loan/components/loan-info-input"
import { TransactionHistoryTableView } from "../components/TransactionHistoryTableView";
import { DivisorLine } from "@/app/shared/DivisorLine";
import DetailPanel from "../components/loan-details-panel";
import { LoanPaymentsTableView } from "../components/LoanPaymentsTableView/LoanPaymentsTableView";
import { useLoan } from "../LoanContext";


export default function LoanDetailView() {
  const loan:LoanDto = useLoan();


  return (
    <div className={styles.container}>
      <div className={styles.card}>
        
      </div>
      <DetailPanel loan={loan}/>
      <DivisorLine/>
      <div>
        <LoanPaymentsTableView payments={loan.pendingInstallments} currency={loan.currency} />
      </div>
      <DivisorLine />
      <div>
        <TransactionHistoryTableView transactions={loan.transactions} currency={loan.currency} />
      </div>
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
