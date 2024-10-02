<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Invoice</title>
</head>
<body style="font-size: 16px; font-family: arial; line-height: 22px">
<div
        class="header"
        style="
        display: flex;
        justify-content: space-between;
        padding: 30px;
        width: 70%;
        margin: auto;
      "
>
    <div
            style="
          display: flex;
          padding: 50px;
          align-content: center;
          width: 100%;
          flex-wrap: wrap;
          justify-content: center;
          flex-direction: row;
        "
    >
        <div style="width: 200px">
            <img src="i"/>
        </div>
    </div>
    <div style="width: 100%">
        <div style="font-size: 12px; line-height: 18px">
            <div>${supplierCompanyName}</div>
            <div>${supplierAddress}</div>
            <div>NIF : ${nif}</div>
        </div>
        <hr/>
        <div>
            <div>${societyName}</div>
            <div>
                ${location}
            </div>
            <div>${direction}</div>
        </div>
        <hr/>
    </div>
</div>
<div class="body">
    <div
            class="info"
            style="
          display: flex;
          justify-content: space-between;
          width: 70%;
          margin: 0px auto 20px;
        "
    >
        <div style="width: 20%; border: 1px solid #222">
            <div
                    style="
              border-bottom: 1px solid #222;
              padding: 4px 10px;
              text-align: center;
            "
            >
                Num. Pedido
            </div>
            <div style="padding: 4px 10px; text-align: center">${invoiceNumber}</div>
        </div>
        <div style="width: 20%; border: 1px solid #222">
            <div
                    style="
              border-bottom: 1px solid #222;
              padding: 4px 10px;
              text-align: center;
            "
            >
                Fecha Factura
            </div>
            <div style="padding: 4px 10px; text-align: center">${invoiceDate}</div>
        </div>
        <div style="width: 20%; border: 1px solid #222">
            <div
                    style="
              border-bottom: 1px solid #222;
              padding: 4px 10px;
              text-align: center;
            "
            >
                Cod. Cliente
            </div>
            <div style="padding: 4px 10px; text-align: center">${supplierId}</div>
        </div>
        <div style="width: 20%; border: 1px solid #222">
            <div
                    style="
              border-bottom: 1px solid #222;
              padding: 4px 10px;
              text-align: center;
            "
            >
                NIF
            </div>
            <div style="padding: 4px 10px; text-align: center">${nif}</div>
        </div>
    </div>
    <table
            style="
          margin: 0px auto 20px;
          width: 70%;
          border: 1px solid black;
          border-collapse: collapse;
        "
    >
        <thead>
        <tr style="border: 1px solid black; border-collapse: collapse">
            <td
                    style="
                border: 1px solid black;
                border-collapse: collapse;
                padding: 10px 20px;
              "
            >
                C O N C E P T O
            </td>
            <td
                    style="
                border: 1px solid black;
                border-collapse: collapse;
                padding: 10px;
                text-align: center;
              "
            >
                UNID.
            </td>
            <td
                    style="
                border: 1px solid black;
                border-collapse: collapse;
                padding: 10px;
                text-align: center;
              "
            >
                PRECIO
            </td>
            <td
                    style="
                border: 1px solid black;
                border-collapse: collapse;
                padding: 10px;
                text-align: center;
              "
            >
                IMPORTE
            </td>
        </tr>
        </thead>
        <tbody >
        <#list  quantities as q >
        <tr >
            <td style="
                border: 1px solid black;
                border-collapse: collapse;
                padding: 10px 20px;
              "
            >
                <div style="line-height: 30px" >
                    <div t></div>
                    <div style="margin-left: 40px">${q.name}</div>
                    <div style="margin-left: 40px">Oferta nº: 202206020001</div>
                    <div style="margin-left: 40px">
                        Dirección: C/ BERNA 2 PORTAL 5 3º A, ARGANDA DEL REY, Madrid
                    </div>
                </div>
            </td>
            <td
                    style="
                border: 1px solid black;
                border-collapse: collapse;
                padding: 10px;
                text-align: center;
              "
            >
                1
            </td>
            <td style="
                border: 1px solid black;
                border-collapse: collapse;
                padding: 10px;
                text-align: center;
              "

            >${q.price}
            </td>
            <td style="
                border: 1px solid black;
                border-collapse: collapse;
                padding: 10px;
                text-align: center;
              ">
                ${q.amount}
            </td>
        </tr>
        </#list>
        </tbody>
    </table>
    <div
            class="totals"
            style="
          display: flex;
          width: 70%;
          margin: 0px auto;
          justify-content: space-between;
        "
    >
        <div>
            <table
                    style="
              border: 1px solid black;
              border-collapse: collapse;
              padding: 10px;
              text-align: center;
            "
            >
                <thead>
                <tr
                        style="
                  border: 1px solid black;
                  border-collapse: collapse;
                  padding: 10px;
                  text-align: center;
                "
                >
                    <td
                            style="
                    border: 1px solid black;
                    border-collapse: collapse;
                    padding: 10px;
                    text-align: center;
                  "
                    >
                        Base Imponible
                    </td>
                    <td
                            style="
                    border: 1px solid black;
                    border-collapse: collapse;
                    padding: 10px;
                    text-align: center;
                  "
                    >
                        % IVA
                    </td>
                    <td
                            style="
                    border: 1px solid black;
                    border-collapse: collapse;
                    padding: 10px;
                    text-align: center;
                  "
                    >
                        Cuota IVA
                    </td>
                </tr>
                </thead>
                <tbody>
                <tr
                        style="
                  border: 1px solid black;
                  border-collapse: collapse;
                  padding: 10px;
                  text-align: center;
                "
                >
                    <td
                            style="
                    border: 1px solid black;
                    border-collapse: collapse;
                    padding: 10px;
                    text-align: center;
                  "
                    >
                        ${taxBase}
                    </td>
                    <td
                            style="
                    border: 1px solid black;
                    border-collapse: collapse;
                    padding: 10px;
                    text-align: center;
                  "
                    >
                       ${tax}
                    </td>
                    <td
                            style="
                    border: 1px solid black;
                    border-collapse: collapse;
                    padding: 10px;
                    text-align: center;
                  "
                    >
                       -
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div>
            <div style="border: 3px solid black">
                <div style="border-bottom: 3px solid black; padding: 10px 20px">
                    T O T A L F A C T U R A
                </div>
                <div style="padding: 10px 20px">${total} €</div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
