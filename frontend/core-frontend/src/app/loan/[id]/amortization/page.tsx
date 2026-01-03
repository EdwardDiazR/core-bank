import React from "react";
import { AmortizationTableView } from "../../components/AmortizationTableView/AmortizationTableView";
import { Loan } from "@/models/Loan";
import { Metadata } from "next";
import { LoanDto } from "@/models/LoanDto";
import { loanService } from "@/services/loanService";

type Props = {
  params: { id: string };
};

export const metadata: Metadata = {
  title: "Tabla de amortizacion",
};

export default async function Amortization({ params }: Props) {
  const { id } = await params;

  const response = await loanService.getLoanByPublicId(id);
  const loan: LoanDto = response.data;

  return <AmortizationTableView amortizationTable={loan.amortizationTable} currency={loan.currency} loanId={id} />;
}
