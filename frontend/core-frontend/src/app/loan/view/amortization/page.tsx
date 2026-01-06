
import React from "react";
import { Loan } from "@/models/Loan";
import { Metadata } from "next";
import { LoanDto } from "@/models/LoanDto";
import { loanService } from "@/services/loans/loanService";
import { cookies } from "next/headers";
import { useLoan } from "../LoanContext";
import { AmortizationTableView } from "@/components/loan/AmortizationTableView/AmortizationTableView";

type Props = {
  params: { id: string };
};

export const metadata: Metadata = {
  title: "Tabla de amortizacion",
};

export default  function Amortization({ params }: Props) {

  return <AmortizationTableView/>;
}
