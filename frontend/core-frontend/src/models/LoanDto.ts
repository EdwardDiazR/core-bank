import { AmortizationTable } from "./AmortizationTable";

export interface LoanDto {
  number: string;
  principalAmount: number;
  outstandingPrincipalBalance: number;
  dueDate: Date;
  financialProduct: financialProduct;
  installmentAmount: number;
  interestBalance: number;
  interestRate: number;
  nextPaymentDate: Date;
  paymentsMade: number;
  paymentsPending: number;
  status: string;
  currency: string;
  termInMonths: number;
  lastPaymentDate:Date,
  availableAmountForDisbursement:number,
  lastInterestRateReviewDate:Date
  ,amortizationTable:AmortizationTable
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
