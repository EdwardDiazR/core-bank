import { AmortizationTable } from "./AmortizationTable";
import { LoanPayment } from "./LoanPayment";
import { TransactionHistory } from "./TransactionHistory";

export interface LoanDto {
  number: string;
  type:string
  principalAmount: number;
  outstandingPrincipalBalance: number;
  dueDate: string;
  financialProduct: financialProduct;
  installmentAmount: number;
  interestBalance: number;
  interestRate: number;
  nextPaymentDate: string;
  paymentsMade: number;
  paymentsPending: number;
  status: string;
  currency: string;
  termInMonths: number;
  lastPaymentDate:string,
  availableAmountForDisbursement:number,
  lastInterestRateReviewDate:string,
  amortizationTable:AmortizationTable
  pendingInstallments:LoanPayment[]
  ,transactions:TransactionHistory[]
}

export interface financialProduct {
  closedAt: Date;
  createdAt: Date;
  id: number;
  principalCustomerId: number;
  productNumber: string;
  productType: string;
  signType: string;
}
