import { AmortizationTable } from '@/models/AmortizationTable';
import { formatNumberWithCurrency } from '@/utils/numberFormatter';
import React from 'react'


type Props = {
  amortizationTable: AmortizationTable;
  currency:string
};

export const AmortizationTableView = ({amortizationTable,currency}:Props) => {
  return (
      <div className="amortizationTable">
        <div className="h-[550px] overflow-y-auto  border-gray-200 rounded-lg shadow-md">
          <table className="min-w-full">
            <thead className="bg-gray-100">
              <tr>
                <th className="px-4 py-2 text-left text-gray-700 font-medium">Pago No.</th>
                <th className="px-4 py-2 text-left text-gray-700 font-medium">Monto cuota</th>
                <th className="px-4 py-2 text-left text-gray-700 font-medium">Capital</th>
                <th className="px-4 py-2 text-left text-gray-700 font-medium">Interes</th>
                <th className="px-4 py-2 text-left text-gray-700 font-medium">Saldo</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {amortizationTable &&
                amortizationTable.items &&
                amortizationTable.items.map((item, index) => {
                  console.log(item);
                  return (
                    <tr key={index} className="hover:bg-gray-50 transition-colors">
                      <td className="px-4 py-2 text-black">{item.installmentNumber}</td>
                      <td className="px-4 py-2 text-black">{formatNumberWithCurrency(item.cuota,currency)}</td>
                      <td className="px-4 py-2 text-black">{formatNumberWithCurrency(item.capital, currency)}</td>
                      <td className="px-4 py-2 text-black">{formatNumberWithCurrency(item.interes,currency)}</td>
                      <td className="px-4 py-2 text-black">{formatNumberWithCurrency(item.saldo, currency)}</td>
                    </tr>
                  );
                })}
            </tbody>
          </table>
        </div>
      </div>
  )
}
