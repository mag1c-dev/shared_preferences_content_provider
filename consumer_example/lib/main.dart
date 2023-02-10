import 'package:flutter/material.dart';
import 'package:shared_preferences_content_provider/shared_preferences_content_provider.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final keyController = TextEditingController();
  String? value;

  @override
  void initState() {
    super.initState();
    SharedPreferencesContentProvider.init(
      providerAuthority: 'your_authority',
    );

    SharedPreferencesContentProvider.listen(
      (event) {
        print("CONSUMER: $event");
        setState(() {
          value = 'Receive changed: $event';
        });
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example consumer app'),
        ),
        body: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            children: [
              TextField(
                decoration: const InputDecoration(hintText: 'Key'),
                controller: keyController,
              ),
              Row(
                mainAxisSize: MainAxisSize.max,
                children: [
                  Expanded(
                      child: ElevatedButton(
                    onPressed: () async {
                      if (keyController.text.isNotEmpty) {
                        final res = await SharedPreferencesContentProvider.get(
                            keyController.text);
                        setState(() {
                          value = 'Value: $res';
                        });
                      } else {
                        final res =
                            await SharedPreferencesContentProvider.getAll();
                        setState(() {
                          value = res.toString();
                        });
                      }
                    },
                    child: Text('Get'),
                  ))
                ],
              ),
              value != null ? Text(value!) : SizedBox(),
            ],
          ),
        ),
      ),
    );
  }
}
