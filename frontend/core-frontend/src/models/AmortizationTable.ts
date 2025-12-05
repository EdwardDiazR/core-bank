export interface AmortizationTable {
  id: number;
  loanId: number;
  item: AmortizationTableItem[];
}

export interface AmortizationTableItem {
  reference: string;             // UUID → string
  installmentNumber: number;
  cuota: number;                 // BigDecimal → number
  capital: number;
  interes: number;
  lateFeeAmount: number;
  saldo: number;
  paymentDate: string;           // LocalDate → string (ISO date)
  isPaid: boolean;
  paidDate: string | null;       // puede ser null si no está pagado
}
