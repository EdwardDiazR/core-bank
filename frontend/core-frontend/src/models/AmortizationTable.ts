export interface AmortizationTable {
  loanNumber: number;
  items: AmortizationTableItem[];
}

export interface AmortizationTableItem {
  id: number;
  installmentNumber: number;
  cuota: number;
  capital: number;
  interes: number;
  charges: number;
  saldo: number;
  paymentDate: Date;
  paidDate: Date | null;
  paid: boolean; // puede ser null si no está pagado
}
