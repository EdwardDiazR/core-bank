// app/loan/[id]/page.tsx
import type { Metadata } from "next";
import * as loanService from "@/services/loanService";
import LoanDetailView from "./LoanDetailView";
import { LoanDto } from "@/models/LoanDto";

type Props = {
  params: { id: string };
};

export const metadata: Metadata = {
  title: "Detalle del préstamo",
};

export default async function LoanDetailPage({ params }: Props) {
  const { id } = await params; // ✅ CLAVE
  console.log(id);

  const response = await loanService.getLoanByPublicId(id);
  const loan: LoanDto = response.data.data;

  return (
    <>
      <LoanDetailView loan={loan} />
    </>
  );
}
