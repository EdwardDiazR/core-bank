// app/loan/[id]/LoanDetailView.tsx
"use server";

import styles from "../page.module.css";
import { LoanDto } from "@/models/LoanDto";
import { formatNumberWithCurrency } from "@/utils/numberFormatter";
import { CustomInput } from "../components/CustomInput";
import { LoanPaymentsTableView } from "../components/LoanPaymentsTableView/LoanPaymentsTableView";
import { TransactionHistoryTableView } from "../components/TransactionHistoryTableView";
import { DivisorLine } from "@/app/shared/DivisorLine";
import DetailPanel from "../components/DetailPanel";

type Props = {
  loan: LoanDto;
};

export default async function LoanDetailView({ loan }: Props) {
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
