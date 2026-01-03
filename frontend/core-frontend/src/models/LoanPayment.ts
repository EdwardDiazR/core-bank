export interface LoanPayment {
  installmentAmount: number;
  outstandingPrincipalDue: number;
  interestDue: number;
  outstandingPrincipalPaid: number;
  interestPaid: number;
  pendingInstallmentBalance: number;
  dueDate: Date;
  lastPaymentDate: Date;
  status:string
}
