"use client";

import { createContext, useContext } from "react";
import { LoanDto } from "@/models/LoanDto";

const LoanContext = createContext<LoanDto | null>(null);

export function LoanProvider({
  loan,
  children,
}: {
  loan: LoanDto;
  children: React.ReactNode;
}) {
  return (
    <LoanContext.Provider value={loan}>
      {children}
    </LoanContext.Provider>
  );
}

export function useLoan() {
  const ctx = useContext(LoanContext);
  if (!ctx) {
    throw new Error("useLoan must be used inside LoanProvider");
  }
  return ctx;
}
