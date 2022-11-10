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
  final valueController = TextEditingController();
  String? notify;

  @override
  void initState() {
    super.initState();
    SharedPreferencesContentProvider.init(
      providerAuthority:
          'com.zhgwu.shared_preferences_content_provider.example',
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            children: [
              TextField(
                decoration: const InputDecoration(hintText: 'Key'),
                controller: keyController,
              ),
              TextField(
                decoration: const InputDecoration(hintText: 'value'),
                controller: valueController,
              ),
              Row(
                mainAxisSize: MainAxisSize.max,
                children: [
                  Expanded(
                    child: ElevatedButton(
                      onPressed: () async {
                        final result =
                            await SharedPreferencesContentProvider.putString(
                                keyController.text, valueController.text);
                        notify = result;
                        setState(() {
                          notify = result;
                        });
                      },
                      child: Text('Put'),
                    ),
                  ),
                  Expanded(
                      child: ElevatedButton(
                    onPressed: () async {
                      if (keyController.text.isNotEmpty) {
                        final res = await SharedPreferencesContentProvider.get(
                            keyController.text);
                        setState(() {
                          notify = 'Key: ${valueController.text}, Value: $res';
                        });
                      } else {
                        final res =
                            await SharedPreferencesContentProvider.getAll();
                        setState(() {
                          notify = res.toString();
                        });
                      }
                    },
                    child: Text('Get'),
                  ))
                ],
              ),
              notify != null ? Text(notify!) : SizedBox(),
            ],
          ),
        ),
      ),
    );
  }
}
