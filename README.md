# Port
Сервисы моделирующие работу порта и определяет оптимальное количество кранов для разгрузки судна.

Программа представляет собой 3 отдельных сервиса, моделирующие работу порта. Сервисы 1 и 2 представляет собой веб сервисы, к которым обращается 3 сервис для получения информации и сохранения результатов моделирования. Порт получает расписание прибытия кораблей на последующие 30 дней, задача программы состоит в том, чтобы, совершив моделирование работы порта, определить оптимальное количество  кранов для разгрузки этих кораблей. 

Таким образом, была написана программа на языке Java с использованием Spring framework. Программа осуществляет подбор оптимального количества кранов для определенного расписания с помощью симуляции прихода и разгрузки кораблей в порту.
