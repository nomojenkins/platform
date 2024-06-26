---
title: 'Инструкция CONSTRAINT'
---

Инструкция `CONSTRAINT` - создание [ограничения](Constraints.md).

### Синтаксис

```
CONSTRAINT eventClause constraintExpr [CHECKED [BY propertyId1, ..., propertyIdN]] MESSAGE messageExpr
    [PROPERTIES outPropertyId1, ..., outPropertyIdM];
```

### Описание

Инструкция `CONSTRAINT` создает ограничение. При нарушении ограничения пользователю будет выдано указанное в инструкции [сообщение](Constraints.md#message).

Также с помощью опции `CHECKED` можно использовать созданное ограничение при вызове диалогов изменения свойств, изменение значений которых может нарушить ограничение. В этом случае в диалоге будет устанавливаться дополнительный фильтр, таким образом, чтобы при изменении значения свойства не нарушить созданное ограничение. Если необходимо ограничить набор свойств, для которых будет выполняться указанная выше фильтрация, то список свойств может быть указан после ключевого слова `BY`.


:::info
Создание ограничения во многом аналогично следующей инструкции:

```lsf
constraintProperty = constraintExpr;
WHEN eventClause [=GROUP MAX constraintProperty(...)]() DO {
    PRINT outConstraintPropertyForm MESSAGE NOWAIT;
    CANCEL;
}
```

но при этом имеет [ряд преимуществ](Constraints.md).
:::

### Параметры

- `eventClause`

    [Блок описания события](Event_description_block.md). Описывает [событие](Events.md), при наступлении которого будет проверяться создаваемое ограничение.

- `constraintExpr`

    [Выражение](Expression.md), значение которого является условием создаваемого ограничения. Если полученное свойство не содержит внутри [оператора `PREV`](PREV_operator.md), то платформа автоматически оборачивает его в [оператор `SET`](Change_operators_SET_CHANGED_etc.md).

- `propertyId1, ..., propertyIdN`

    Список [идентификаторов свойств](IDs.md#propertyid). Для каждого перечисленного свойства в диалоге изменения свойства будет выполняться фильтрация вариантов, которые нарушают создаваемое ограничение.

- `messageExpr`

    Выражение, значение которого выдаётся в качестве сообщения пользователю, когда заданное ограничение нарушается. Может быть [строковым литералом](IDs.md#strliteral) либо свойством без параметров.

- `outPropertyId1, ..., outPropertyIdM`

    Список идентификаторов свойств, значения которых выдаются в сообщении пользователю, когда заданное ограничение нарушается. Если список не указывается, то выбираются свойства, подходящие по классам параметров к условию ограничения и принадлежащие [группе свойств](Groups_of_properties_and_actions.md) `System.id`.

### Примеры

```lsf
// остаток не меньше 0
CONSTRAINT balance(Sku s, Stock st) < 0 MESSAGE 'Остаток не может быть отрицательным для ' + 
    (GROUP CONCAT 'Товар: ' + name(Sku ss) + ' Склад: ' + name(Stock sst), '\n' IF SET(balance(ss, sst) < 0) ORDER sst);

barcode = DATA STRING[15] (Sku);
// "эмуляция" политики безопасности
CONSTRAINT DROPCHANGED(barcode(Sku s)) AND name(currentUser()) != 'admin' 
    MESSAGE 'Изменять штрих-код для уже созданного товара разрешено только администратору';

sku = DATA Sku (OrderDetail);
in = DATA BOOLEAN (Sku, Customer);

CONSTRAINT sku(OrderDetail d) AND NOT in(sku(d), customer(order(d)))
    CHECKED BY sku[OrderDetail] // будет применяться фильтр по доступным sku при выборе товара для строки заказа
    MESSAGE 'В заказе выбран недоступный пользователю товар для выбранного покупателя';
```

