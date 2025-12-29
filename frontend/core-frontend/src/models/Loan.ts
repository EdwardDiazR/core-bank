import { AmortizationTable } from "./AmortizationTable";

export interface Loan {
  id: number;
  number: string;
  publicId: string;
  principalCustomerId: number;
  productType: "LOAN";
  createdAt: string;
  closedAt: string | null;
  signType: string;
  relatives: LoanRelative[];
  status: string;
  type: string;
  currency: string;
  principalAmount: number;
  availableAmountForDisbursement: number;
  outstandingPrincipalAmount: number;
  interestBalance: number;
  interestRate: number;
  termInMonths: number;
  paymentFrequency: string;
  dailyInterestFactor: number;
  installmentAmount: number;
  lateFeeRate: number;
  lateFeeBalance: number;
  totalInstallmentBalance: number;
  totalPaidInterest: number;
  projectedInterest: number;
  oneCycleTimes: number;
  twoCycleTimes: number;
  paymentsMade: number;
  paymentsPending: number;
  firstPaymentDate: string | null;
  nextPaymentDate: string | null;
  lastPaymentDate: string | null;
  lastInterestBalanceUpdateDate: string | null;
  disbursementDate: string | null;
  lastInterestRateReviewDate: string | null;
  dueDate: string | null;
  updatedAt: string | null;
  linkedAccount: string | null;
  canAutoDebit: boolean;
  isLineOfCredit: boolean;
  isDeleted: boolean;
  amortizationTable: AmortizationTable | null;
}

export interface LoanRelative {
  id: string;
  customerId: number;
  relationCondition: string;
  principal: boolean;
}


