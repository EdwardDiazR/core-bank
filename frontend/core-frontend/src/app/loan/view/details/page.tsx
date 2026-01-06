import type { Metadata } from "next";

import { LoanDto } from "@/models/LoanDto";
import { loanService } from "@/services/loans/loanService";
import { cookies } from "next/headers";
import LoanDetailView from "./loan-detail-view.page";
import { LoadingSpinner } from "@/app/shared/loading-spinner";
import { useLoan } from "../LoanContext";

type Props = {
  params: { id: string };
};

export const metadata: Metadata = {
  title: "Detalle del préstamo",
};

export default function LoanDetailPage({ params }: Props) {


  return (
    <>
      <LoanDetailView />

      {/* {!loan && (
        <LoadingSpinner/>
      )} */}
    </>
  );
}
