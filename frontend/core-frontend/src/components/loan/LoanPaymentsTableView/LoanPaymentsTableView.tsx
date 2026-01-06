import { formatNumberWithCurrency } from "@/utils/numberFormatter";
import React from "react";
import styles from "./page.module.css";
import { LoanPayment } from "@/models/LoanPayment";
import clsx from "clsx";

type Props = {
  payments: LoanPayment[];
  currency: string;
};

export const LoanPaymentsTableView = ({ payments, currency }: Props) => {
  //   const publicId = "loanId";
  //   const amortizationTableUrl = ``;
  //   const [blobPdfUrl, setBlobPdfUrl] = useState<string>("");

  //   const generateTablePdf = async () => {
  //     // window.open(amortizationTableUrl, "_blank");
  //     if (!blobPdfUrl.length) {
  //       const response = await fetch(`http://localhost:8094/loan/amortization/${publicId}/pdf`);
  //       const blob = await response.blob();
  //       setBlobPdfUrl(URL.createObjectURL(blob));
  //     }
  //   };

  return (
    <div className="flex-1">
      <div className={``}>
        <h2>Pago(s) pendientes</h2>
      </div>

      <div className="h-auto max-h-[550px] overflow-y-auto rounded-lg border border-gray-200 shadow-xl">
        <table className="min-w-full border-collapse max-w-full">
          <thead className="sticky top-0 bg-[#003a8f] z-10">
            <tr>
              <th className="px-4 py-3 text-white text-left text-xs font-semibold uppercase tracking-wide ">
                Fecha Vencimiento
              </th>
              <th className="px-4 py-3 text-white text-center text-xs font-semibold uppercase tracking-wide ">
                Monto pendiente
              </th>
              <th className="px-4 py-3 text-white text-center text-xs font-semibold uppercase tracking-wide ">
                Capital pendiente:
              </th>
              <th className="px-4 py-3 text-white text-center text-xs font-semibold uppercase tracking-wide ">
                Interés pendiente:
              </th>
              <th className="px-4 py-3 text-white text-center text-xs font-semibold uppercase tracking-wide ">
                Status
              </th>
            </tr>
          </thead>

          <tbody className="divide-y divide-gray-200 bg-white">
            {payments.map((item, index) => {
              const checkIfPaymentIsDelinquent = () => {
                return item.dueDate.getDate < new Date().getDate;
              };
              return (
                <tr key={index} className="hover:bg-blue-50 transition-colors cursor-pointer">
                  <td
                    className={clsx("px-4 py-2 text-sm text-gray-800", {
                      "text-red-500 font-semibold": checkIfPaymentIsDelinquent,
                    })}
                  >
                    {item.dueDate.toLocaleString()}
                  </td>

                  <td className="px-4 py-2 text-sm text-gray-800 text-center">
                    {formatNumberWithCurrency(item.pendingInstallmentBalance, currency)}
                  </td>

                  <td className="px-4 py-2 text-sm text-gray-800 text-center">
                    {formatNumberWithCurrency(item.outstandingPrincipalDue - item.outstandingPrincipalPaid, currency)}
                  </td>

                  <td className="px-4 py-2 text-sm text-gray-800 text-center">
                    {formatNumberWithCurrency(item.interestDue - item.interestPaid, currency)}
                  </td>

                  <td className={clsx("px-4 py-2 text-sm font-medium text-gray-900 text-center",{
                    "text-orange-500":item.status == "PARTIAL" 
                  })}>
                    {item.status == "PARTIAL" ? "PARCIAL" : item.status}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};
