
import type { Metadata } from "next";
import LoanDetailView from "./LoanDetailView";
import { LoanDto } from "@/models/LoanDto";
import { loanService } from "@/services/loanService";

type Props = {
  params: { id: string };
};

export const metadata: Metadata = {
  title: "Detalle del préstamo",
};

export default async function LoanDetailPage({ params }: Props) {
  const { id } = await params; // ✅ CLAVE
  // console.log(id);

  const response = await loanService.getLoanByPublicId(id);
  const loan: LoanDto =  response.data

  return (
    <>
      <LoanDetailView loan={loan} />
    </>
  );
}
