import { TransactionHistory } from "@/models/TransactionHistory";
import { formatNumberWithCurrency } from "@/utils/numberFormatter";
import clsx from "clsx";
import React from "react";

export const TransactionHistoryTableView = ({
  transactions,
  currency,
}: {
  transactions: TransactionHistory[];
  currency: string;
}) => {
  return (
    <div className="flex-1">
      <div className={``}>
        <h2>Historial</h2>
      </div>

      <div className="h-auto max-h-[550px] overflow-y-auto rounded-lg border border-gray-200 shadow-xl">
        <table className="min-w-full border-collapse max-w-full">
          <thead className="sticky top-0 bg-[#003a8f] z-10">
            <tr>
              <th className="px-4 py-3 text-white text-center text-xs font-semibold uppercase tracking-wide ">Fecha</th>

          
              <th className="px-4 py-3 text-white text-center text-xs font-semibold uppercase tracking-wide ">
                Concepto
              </th>
              <th className="px-4 py-3 text-white text-center text-xs font-semibold uppercase tracking-wide ">Monto</th>
              <th className="px-4 py-3 text-white text-center text-xs font-semibold uppercase tracking-wide ">Saldo</th>
            </tr>
          </thead>

          <tbody className="divide-y divide-gray-200 bg-white">
            {transactions.map((item, index) => {
              return (
                <tr key={index} className="hover:bg-blue-50 transition-colors cursor-pointer">
                  <td className={clsx("px-4 py-2 text-sm text-gray-800", {})}>
                    {new Date(item.date).toLocaleDateString("es-DO",{})}
                  </td>

                  <td className="px-4 py-2 text-m text-[15px] font-medium text-slate-900  text-center">{item.description}</td>

                  <td className="px-4 py-2 text-sm text-gray-800 text-center">
                    {formatNumberWithCurrency(item.amount, currency)}
                  </td>

                  <td className="px-4 py-2 text-sm text-gray-800 text-center">
                    {formatNumberWithCurrency(item.afterBalance, currency)}
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
