
import React from "react";
import { AmortizationTableView } from "../components/AmortizationTableView/AmortizationTableView";
import { Loan } from "@/models/Loan";
import { Metadata } from "next";
import { LoanDto } from "@/models/LoanDto";
import { loanService } from "@/services/loans/loanService";
import { cookies } from "next/headers";
import { useLoan } from "../LoanContext";

type Props = {
  params: { id: string };
};

export const metadata: Metadata = {
  title: "Tabla de amortizacion",
};

export default  function Amortization({ params }: Props) {

  return <AmortizationTableView/>;
}
